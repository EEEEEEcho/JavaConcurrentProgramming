package com.echo.review;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileReadDemo {
    public static void main(String[] args)  throws Exception{
        Map<String, Integer> map = readFile("F:\\JavaConcurrentProgramming\\src\\main\\java\\com\\echo\\review\\testfiles");
        System.out.println(map);
    }
    public static Map<String,Integer> readFile(String fpath) throws Exception{
        Path path = Paths.get(fpath);
        //获取当前目录下的所有文件
        File[] files = path.toFile().listFiles();
        //定义计数器
        CountDownLatch latch = new CountDownLatch(files.length);
        //定义线程池
        ExecutorService pool = Executors.newFixedThreadPool(5);
        //定义接受结果的列表
        List<Future<Map<String,Integer>>> results = new ArrayList<>();
        for (File file : files) {
            Future<Map<String, Integer>> future = pool.submit(new ReadTask(file.toPath(), latch));
            results.add(future);
        }
        //等待所有任务执行完
        latch.await();
        //合并结果
        Map<String,Integer> ans = null;
        for (int i = 0; i < results.size(); i++) {
            ans = mergeMap(ans,results.get(i).get());
        }
        return ans;
    }
    public static Map<String,Integer> mergeMap(Map<String,Integer> map1,Map<String,Integer> map2){
        if (map1 == null) return map2;
        if (map2 == null) return map1;
        return Stream.concat(map1.entrySet().stream(), map2.entrySet().stream())
                .collect(Collectors.toMap(Map.Entry::getKey,Map.Entry::getValue,Integer::sum));
    }
}
class ReadTask implements Callable<Map<String,Integer>>{
    private Path path;
    private CountDownLatch latch;
    private Map<String,Integer> map = new ConcurrentHashMap<>();

    public ReadTask(Path path,CountDownLatch latch){
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
            while (scanner.hasNext()){
                String line = scanner.nextLine();
                String[] strings = line.split(" ");
                Arrays.stream(strings).filter(e -> !"".equals(e)).forEach(
                        str -> {
                            str = preProcess(str);
                            map.put(str,map.getOrDefault(str,0) + 1);
                        }
                );
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
