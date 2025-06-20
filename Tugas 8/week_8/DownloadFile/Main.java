package week_8.DownloadFile;

import java.util.*;
import java.util.concurrent.*;
import week_8.DownloadFile.*;

public class Main {

    private static int successfulDownloads = 0;
    private static int completedDownloads = 0;
    private static int totalFiles = 0;
    private static boolean allDone = false;
    private static ArrayList<Result> results = new ArrayList<>();

    public static synchronized void recordResult(int fileId, int duration, String threadName) {
        completedDownloads++;
        String status;
        if (duration <= 2) {
            status = "Success";
            successfulDownloads++;
        } else {
            status = "Timeout";
        }
        Result result = new Result(fileId, threadName, duration, status);
        results.add(result);
    }

    public static void main(String[] args) {

        Scanner x = new Scanner(System.in);
        System.out.print("Enter the number of files to download: ");
        totalFiles = x.nextInt();
        x.nextLine();

        ExecutorService downloadExecutor = Executors.newFixedThreadPool(3);
        ExecutorService uiExecutor = Executors.newSingleThreadExecutor();

        long startTime = System.currentTimeMillis();

        uiExecutor.submit(() -> {
            int time = 0;
            while (!allDone) {
                System.out.printf("Downloading files....(%ds)%n", time);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
                time++;
            }
        });

        for (int i = 1; i <= totalFiles; i++) {
            int fileId = i;
            downloadExecutor.submit(() -> {
                int duration = new Random().nextInt(3) + 1;
                try {
                    Thread.sleep(duration * 1000L);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                String threadName = Thread.currentThread().getName();
                recordResult(fileId, duration, threadName);
            });
        }

        downloadExecutor.shutdown();

        try {
            if (!downloadExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
                downloadExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            downloadExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        allDone = true;
        uiExecutor.shutdown();
        try {
            uiExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            uiExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }

        long endTime = System.currentTimeMillis();
        int totalTime = (int) ((endTime - startTime) / 1000);

        System.out.println("--------------------------------------------------");
        System.out.println("                  Detailed Report                 ");
        System.out.println("--------------------------------------------------");
        System.out.printf("%-8s | %-18s | %-8s | %-8s%n",
                "File ID", "Thread", "Duration", "Status");
        System.out.println("--------------------------------------------------");

        results.sort(Comparator.comparingInt(r -> r.fileId));

        for (Result r : results) {
            System.out.printf("%-8d | %-18s | %-8s | %-8s%n",
                    r.fileId, r.threadName, r.duration + "s", r.status);
        }

        System.out.println("--------------------------------------------------");
        System.out.println("                     Summary                      ");
        System.out.println("--------------------------------------------------");
        System.out.println("Successful downloads : " + successfulDownloads);
        System.out.println("Failed downloads     : " + (totalFiles - successfulDownloads));
        System.out.printf("Total time           : %ds%n", totalTime);
    }
}