package daily.boot.gulimall.search.threads;

import java.util.Objects;
import java.util.concurrent.*;

public class TreadTest {
    private static final ExecutorService executor = new ThreadPoolExecutor(5, 10, 1L, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(10));
    
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        System.out.println("main...start...");
        asyncThenApply();
        //asyncThenAccept();
        //asyncThenRun();
        //asyncHandle();
        //asyncKnowledge();
        System.out.println("main...end...");
    }
    
    /**
     * 接收上一个异步任务的结果，消费结果，并返回新的completableFuture
     */
    public static void asyncThenApply() throws ExecutionException, InterruptedException {
        String rtn = CompletableFuture.supplyAsync(() -> {
            System.out.printf("当前线程。。。%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果。。。" + i);
            return i;
        }, executor).thenApplyAsync(res -> {
            String result = "Hello:" + res;
            printThreadInfo();
            System.out.printf("thenAcceptAsync---上一个异步任务完成，消费结果:%d，并返回新值:%s\n", res, result);
            return result;
        }, executor).get();
        System.out.println("异步任务最终返回结果:" + rtn);
    
    }
    /**
     * 接收上一个异步任务的结果，消费结果，但无返回
     */
    public static void asyncThenAccept() {
        CompletableFuture.supplyAsync(() -> {
            System.out.printf("当前线程。。。%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果。。。" + i);
            return i;
        }, executor).thenAcceptAsync((res) -> {
            printThreadInfo();
            System.out.printf("thenAcceptAsync---上一个异步任务完成，消费结果但无返回:%d\n", res);
        }, executor);
        
    }
    
    /**
     * 不接收上一个异步任务的结果，上一个任务结束直接运行
     */
    public static void asyncThenRun() {
        
        CompletableFuture.supplyAsync(() -> {
            System.out.printf("当前线程。。。%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果。。。" + i);
            return i;
        }, executor).thenRunAsync(() -> {
            printThreadInfo();
            System.out.println("thenRunAsync---上一个异步任务完成，接着运行下一个，不关心上一步的结果");
        }, executor);
        
    }
    public static void asyncHandle() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            System.out.printf("当前线程。。。%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            int i = 10 / 2;
            System.out.println("运行结果。。。" + i);
            return i;
        }, executor).handle((res, exp) -> {
            //处理上一步返回结果
            if (Objects.nonNull(res)) {
                System.out.printf("异步任务成功完成...结果是:[%d]\n", res);
                return res * 2;
            } else if (Objects.nonNull(exp)) {
                System.out.printf("异步任务发生异常%s，返回[-1]:\n", exp);
                return -1;
            }
            System.out.println("异步任务成功完成，无返回值，返回默认值:[0]");
            return 0;
        });
        Integer result = future.get();
        System.out.println("异步任务最终返回值...:" + result);
        
        
    }
    public static void asyncKnowledge() {
        System.out.println("main...start...");
        CompletableFuture.supplyAsync(() -> {
            System.out.printf("当前线程。。。%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            int i = 10 / 0;
            System.out.println("运行结果。。。" + i);
            return i;
        }, executor).whenCompleteAsync((result, exception) -> {
            System.out.printf("whenCompleteAsync线程信息%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            System.out.printf("异步任务成功完成...结果是:[%d]...异常信息:[%s]\n", result, exception);
        }, executor).exceptionally(exception -> {
            //exceptionally在异常发生时，可以返回默认值
            Integer defaultVal = 10;
            System.out.printf("exceptionally线程信息%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
            System.out.printf("异步任务发生异常%s，返回默认值[%d]:\n", exception, defaultVal);
            return defaultVal;
        });
        System.out.println("main...end...");
        
    }
    
    private static void printThreadInfo() {
        System.out.printf("当前线程。。。%s-[%d]\n", Thread.currentThread().getName(), Thread.currentThread().getId());
    }
}
