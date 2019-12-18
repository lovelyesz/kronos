package com.yz.kronos.config;

import com.yz.kronos.model.ExecuteLogModel;
import com.yz.kronos.model.FlowInfoModel;
import com.yz.kronos.service.ExecuteLogService;
import com.yz.kronos.service.FlowInfoService;
import com.yz.kronos.service.ScheduleService;
import com.yz.kronos.util.SpringHelperUtil;
import io.termd.core.function.BiConsumer;
import io.termd.core.function.Consumer;
import io.termd.core.readline.Keymap;
import io.termd.core.readline.Readline;
import io.termd.core.tty.TtyConnection;
import io.termd.core.tty.TtyEvent;
import io.termd.core.util.Helper;
import org.springframework.context.ApplicationContext;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A terminal that mimics a shell, shows various aspects of Termd:
 *
 * <ul>
 * <li>{@link Readline} usage</li>
 * <li>{@link TtyConnection} usage</li>
 * </ul>
 */
public class Shell implements Consumer<TtyConnection> {

    private static final Pattern splitter = Pattern.compile("\\w+");

    @Override
    public void accept(final TtyConnection conn) {
        InputStream inputrc = Keymap.class.getResourceAsStream("inputrc");
        Keymap keymap = new Keymap(inputrc);
        Readline readline = new Readline(keymap);
        for (io.termd.core.readline.Function function : Helper.loadServices(Thread.currentThread().getContextClassLoader(), io.termd.core.readline.Function.class)) {
            readline.addFunction(function);
        }
        conn.write("Welcome to Term.d shell example\n\n");
        read(conn, readline);
    }

    /**
     * Use {@link Readline} to read a user input and then process it
     *
     * @param conn     the tty connection
     * @param readline the readline object
     */
    public void read(final TtyConnection conn, final Readline readline) {

        // Just call readline and get a callback when line is read
        readline.readline(conn, "kronos> ", new Consumer<String>() {
            @Override
            public void accept(String line) {
                // Ctrl-D
                if (line == null) {
                    conn.write("logout\n").close();
                    return;
                }

                Matcher matcher = splitter.matcher(line);
                if (matcher.find()) {
                    String cmd = matcher.group();

                    // Gather args
                    List<String> args = new ArrayList<String>();
                    while (matcher.find()) {
                        args.add(matcher.group());
                    }

                    try {
                        new Task(conn, readline, Command.valueOf(cmd), args).start();
                        return;
                    } catch (IllegalArgumentException e) {
                        conn.write(cmd + ": command not found\n");
                    }
                }
                read(conn, readline);
            }
        });
    }

    /**
     * A blocking interruptible task.
     */
    class Task extends Thread implements BiConsumer<TtyEvent, Integer> {

        final TtyConnection conn;
        final Readline readline;
        final Command command;
        final List<String> args;
        volatile boolean running;

        public Task(TtyConnection conn, Readline readline, Command command, List<String> args) {
            this.conn = conn;
            this.readline = readline;
            this.command = command;
            this.args = args;
        }

        @Override
        public void accept(TtyEvent event, Integer cp) {
            switch (event) {
                case INTR:
                    if (running) {
                        // Ctrl-C interrupt : we use Thread interrupts to signal the command to stop
                        interrupt();
                    }
            }
        }

        @Override
        public void run() {

            // Subscribe to events, in particular Ctrl-C
            conn.setEventHandler(this);
            running = true;
            try {
                command.execute(conn, args);
            } catch (InterruptedException e) {
                // Ctlr-C interrupt
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running = false;
                conn.setEventHandler(null);

                // Readline again
                read(conn, readline);
            }
        }
    }

    /**
     * scheduled {id}：执行某个工作流
     * The shell app commands.
     */
    enum Command {

        /**
         * flow：查看工作流列表
         * flow {id}：查看工作流信息
         */
        flow() {
            @Override
            public void execute(TtyConnection conn, List<String> args) {
                ApplicationContext applicationContext = SpringHelperUtil.getApplicationContext();
                FlowInfoService flowInfoService = applicationContext.getBean(FlowInfoService.class);
                if (args.size()==0){
                    List<FlowInfoModel> flowInfoModelList = flowInfoService.list(new FlowInfoModel());
                    flowInfoModelList.forEach(flowInfoModel -> {
                        conn.write(flowInfoModel.toString()+"\n");
                    });
                }else {
                    FlowInfoModel flowInfoModel = flowInfoService.get(Long.valueOf(args.get(0)));
                    conn.write(flowInfoModel.toString()+"\n");
                }
            }
        },
        start() {
            @Override
            void execute(TtyConnection conn, List<String> args) throws Exception {
                ApplicationContext applicationContext = SpringHelperUtil.getApplicationContext();
                ScheduleService scheduleService = applicationContext.getBean(ScheduleService.class);
                final Long flowId = Long.valueOf(args.get(0));
                scheduleService.runFlow(flowId);
                conn.write("flow "+flowId+" has scheduled \n");
            }
        },
        stop() {
            @Override
            void execute(TtyConnection conn, List<String> args) throws Exception {
                ScheduleService scheduleService = SpringHelperUtil.getApplicationContext().getBean(ScheduleService.class);
                Long execId = Long.valueOf(args.get(0));
                scheduleService.stopFlow(execId);
                conn.write("exec "+execId+" has shutdown \n");
            }
        },
        log(){
            @Override
            void execute(TtyConnection conn, List<String> args) throws Exception {
                ExecuteLogService executeLogService = SpringHelperUtil.getApplicationContext().getBean(ExecuteLogService.class);
                if(args.size()>0){
                    Long execId = Long.valueOf(args.get(0));
                    ExecuteLogModel executeLogModel = executeLogService.get(execId);
                    conn.write(executeLogModel+"\n");
                }else {
                    final List<ExecuteLogModel> executeLogModelList = executeLogService.list(new ExecuteLogModel());
                    executeLogModelList.forEach(executeLogModel -> {
                        conn.write(executeLogModel+"\n");
                    });
                }
            }
        };

        abstract void execute(TtyConnection conn, List<String> args) throws Exception;
    }
}
