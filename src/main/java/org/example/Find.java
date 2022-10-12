package org.example;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.stream.Stream;

public class Find {

    private final ExecutorService service;

    public Find(ExecutorService service) {
        this.service = service;
    }

    private String checkResource(Path dir, String mask) {
        if (!dir.toFile().exists() || !dir.toFile().isDirectory())
            throw new RuntimeException("Directory " + dir.toFile().getAbsolutePath() + " not found");

        if (mask.charAt(0) == '*') {
            return mask.replaceFirst("\\*", "").toLowerCase();
        }
        return mask.toLowerCase();
    }

    public void walk1(Path dir, String mask) {
        String ext = checkResource(dir, mask);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir)) {

            for (Path path : stream) {
                service.execute(() -> {

                    if (Files.isReadable(path)) {
                        if (path.toFile().isDirectory()) {
                            walk1(path, ext);
                        } else if (path.toFile().getName().toLowerCase().endsWith(ext)) {
//                            System.out.println("File: " + path.toFile().getAbsolutePath() + " " + Thread.currentThread().getName());
                            System.out.println("File: " + path.toFile().getAbsolutePath());
                        }
                    }
                });
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void compare(List<String> paths) {
        Collections.sort(paths);
        paths.forEach(System.out::println);
    }


    public void walk2(Path dir, String mask) {
        String ext = checkResource(dir, mask);

        try (Stream<Path> walk = Files.walk(dir)) {

            walk.parallel()
                    .filter(Files::isReadable)
                    .filter(path -> !Files.isDirectory(path))
                    .filter(path -> path.toFile().getName().toLowerCase().endsWith(ext))
//                    .sorted(new Comparator<Path>() {
//                        @Override
//                        public int compare(Path path1, Path path2) {
//                            return path1.toFile().getAbsolutePath().compareToIgnoreCase(path2.toFile().getAbsolutePath());
//                        }
//                    })
                    .forEach(System.out::println);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}






