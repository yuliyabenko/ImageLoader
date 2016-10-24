import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Yuliya on 19.10.2016.
 */
public class ImageLoader {

    private static String[] arrayUrl = {"http://minionomaniya.ru/wp-content/uploads/2016/01/%D0%9A%D0%B5%D0%B2%D0%B8%D0%BD.jpg",
            "https://files5.adme.ru/files/news/part_53/535055/preview-650x341-98-1457959353.jpg"};

    private static final CyclicBarrier BARRIER = new CyclicBarrier(arrayUrl.length, new DownloadingImg());

    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < arrayUrl.length; i++) {
            new Thread(new Image(i)).start();
            Thread.sleep(2000);
        }
    }

    public static class DownloadingImg implements Runnable {
        @Override
        public void run() {
            loadImage();
            try {
                Thread.sleep(2000);
                System.out.println("All images are loaded.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        public void loadImage() {
            String destination;
            for (int i = 0; i < arrayUrl.length; i++) {
                URL url = null;
                try {
                    url = new URL(arrayUrl[i]);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                InputStream is = null;
                try {
                    is = url.openStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                OutputStream os = null;
                try {
                    destination = "C:/Users/Yuliya/Desktop/image" + i + ".jpg";
                    os = new FileOutputStream(destination);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                byte[] b = new byte[2048];
                int length;

                try {
                    while ((length = is.read(b)) != -1) {
                        os.write(b, 0, length);
                    }
                    is.close();
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static class Image implements Runnable {
        private int imageNumber;

        public Image(int imageNumber) {
            this.imageNumber = imageNumber;
        }

        @Override
        public void run() {
            try {
                System.out.printf("Image ¹%d is ready for loading.\n", imageNumber);
                BARRIER.await();
            } catch (Exception e) {
            }
        }
    }
}
