package org.example;

import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final String TEXT = """
            Правила пользования программой
            
            для запуска поиска требуется указать 3 аргумента через пробел:
            
             1. путь до каталога поиска
             2. расширение искомых файлов
             3. количество потоков поиска
            
            java -jar finder-1.0-SNAPSHOT.jar <path> <extends> <thread>
            
            Пример: java -jar finder-1.0-SNAPSHOT.jar /mnt/sda/files/IdeaProjects/router/ .java 5
            
            """;


    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Missing arguments\n" + TEXT);
            System.exit(0);
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Short.parseShort(args[2]));
        Find find = new Find(executorService);
        find.walk1(Path.of(args[0]), args[1]);

//        find.walk2(Path.of(args[0]), args[1]);
    }

}

