import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

public class Log {
    public void initialEntry(){
        try{
            RandomAccessFile raf = new RandomAccessFile("log.txt","rw");
            Date date = new Date();
            String s = date.toString();
            int len = (int) raf.length();
            raf.seek(len);
            raf.write(("\n\n\n\n============== "+s+" ==============").getBytes());
            raf.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void writeLog(String s, boolean newLine) {
        try{
            RandomAccessFile raf = new RandomAccessFile("log.txt","rw");
            int len = (int) raf.length();
            raf.seek(len);
            if (newLine) {
                raf.write(("\r\n" + s).getBytes());
            }else{
                raf.write((s).getBytes());
            }

            raf.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String[] test() throws IOException {
        String filePath = "log.txt";
        int maxLines = 1000;
        Path path = Paths.get(filePath);
        List<String> allLines = Files.readAllLines(path);

        int totalLines = allLines.size();
        int startIndex = Math.max(0, totalLines - maxLines);

        List<String> lastLines = allLines.subList(startIndex, totalLines);
        return lastLines.toArray(new String[0]);
    }
}