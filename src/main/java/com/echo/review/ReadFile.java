package com.echo.review;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReadFile {
    public static void main(String[] args) throws Exception{
        Map<String, Integer> map = readFile("F:\\JavaConcurrentProgramming\\src\\main\\java\\com\\echo\\review");
        System.out.println(map);
    }
    public static Map<String,Integer> readFile(String path) throws Exception{
        //1.打开目录下的所有文件
        Path dir = Paths.get(path);
        File[] files = dir.toFile().listFiles();
        System.out.println("文件个数： " + files.length);
        //2.定义线程池,
        ExecutorService pool = Executors.newFixedThreadPool(5);
        //3.定义计数器，当完成所有的文件读取后返回
        CountDownLatch latch = new CountDownLatch(files.length);
        //4.将所有的文件交给这5个线程来处理。昨天脑子秀逗了，没有想清楚。盲目的以为五个线程会并发的从文件列表
        //的所有文件中竞争的取文件，没有想明白这五个线程根本不会发生同时读取同一个文件的情况。
        //实际的执行情况应该是：给Files[] files中的每个file创建一个任务。最后会创建files.length个任务
        //然后将这些任务，全部提交到线程池中，这五个线程会从这些任务中五个一组五个一组的执行，每次执行完一个任务之后
        //继续从等待队列中取任务来执行。昨天想的太复杂了。

        //定义接受结果的列表
        List<Future<Map<String,Integer>>> results = new ArrayList<>();
        //提交任务给线程池，并接受结果
        Arrays.stream(files).forEach(f -> results.add(pool.submit(new FileReadTask(f.toPath(),latch))));
        //停止接受新任务
        pool.shutdown();

        //等待所有结果完成
        latch.await();
        Map<String,Integer> result = null;
        for (int i = 0; i < results.size(); i++) {
            result = mergeMap(result,results.get(i).get());
        }
        return result;
    }

    public static Map<String,Integer> mergeMap(Map<String,Integer> map1,Map<String,Integer> map2){
        if (map1 == null) return map2;
        if (map2 == null) return map1;
        return Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue, Integer::sum));
    }

}
//一个任务对象,读取一个文件中的所有内容，并统计数量
class FileReadTask implements Callable<Map<String,Integer>> {
    private Path path;
    private CountDownLatch latch;
    private Map<String,Integer> map = new ConcurrentHashMap<>();

    public FileReadTask(Path path,CountDownLatch latch){
        this.path = path;
        this.latch = latch;
    }

    private String preProcess(String str){
        StringBuilder sb = new StringBuilder();
        for (char c : str.toCharArray()){
            if (Character.isLetter(c)){
                sb.append(c);
            }
        }
        return sb.toString().trim();
    }

    @Override
    public Map<String, Integer> call() throws Exception {
        try(Scanner scanner = new Scanner(Files.newInputStream(path))){
            while (scanner.hasNext()) {
                String s = scanner.nextLine();
                Arrays.stream(s.split(" ")).
                        filter(t -> !"".equals(t)).
                        forEach(word -> {
                            word = preProcess(word);
                            map.put(word,map.getOrDefault(word,0) + 1);
                        });
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            latch.countDown();
        }
        return map;
    }
}