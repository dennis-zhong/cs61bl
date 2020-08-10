import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexPuzzles {
    public static List<String> urlRegex(String[] urls) {
        List<String> lst = new ArrayList<>();
        Pattern pat = Pattern.compile("\\(\\w*?https?://(\\w*?\\.)*?[a-z]{2,3}/\\w+?\\.html\\w*?\\)");

        for(String url:  urls) {
            Matcher mat = pat.matcher(url);
            if(mat.matches()) {
                lst.add(url);
            }
        }
        return lst;
    }

    public static List<String> findStartupName(String[] names) {
        // Create a String pattern to fill return array
        return null;
    }

    public static BufferedImage imageRegex(String filename, int width, int height) {
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(filename)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("No such file found: " + filename);
        }

        // Initialize both Patterns and 3-d array
        Pattern RGB = Pattern.compile("\\[([0-9]{1,3}), ([0-9]{1,3}), ([0-9]{1,3})\\]");
        Pattern loc = Pattern.compile("\\(([0-9]{1,3}), ([0-9]{1,3})\\)");
        int[][][] arr = new int[height][width][3];
        try {
            String line;
            while ((line = br.readLine()) != null) {
                // Initialize both Matchers and find() for each
                Matcher rgbmatcher = RGB.matcher(line);
                Matcher locmatcher = loc.matcher(line);
                while(rgbmatcher.find() && locmatcher.find()) {
                    int x = Integer.valueOf(locmatcher.group(1));
                    int y = Integer.valueOf(locmatcher.group(2));
                    for(int i=0; i<3; i++) {
                        arr[x][y][i] = Integer.valueOf(rgbmatcher.group(i+1));
                    }
                }
            }
        } catch (IOException e) {
            System.err.printf("Input error: %s%n", e.getMessage());
            System.exit(1);
        }
        // Return the BufferedImage of the array
        return arrayToBufferedImage(arr);
    }

    public static BufferedImage arrayToBufferedImage(int[][][] arr) {
        BufferedImage img = new BufferedImage(arr.length,
        	arr[0].length, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr.length; j++) {
                int pixel = 0;
                for (int k = 0; k < 3; k++) {
                    pixel += arr[i][j][k] << (16 - 8*k);
                }
                img.setRGB(i, j, pixel);
            }
        }

        return img;
    }

    public static void main(String[] args) {
        /* For testing image regex */
        BufferedImage img = imageRegex("mystery.txt", 400, 400);

        File outputfile = new File("output_img.jpg");
        try {
            ImageIO.write(img, "jpg", outputfile);
        } catch (IOException e) {
            System.out.println("Error writing file: " + e.getMessage());
        }
    }
}
