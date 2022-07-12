import java.io.*;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;

public class Main {
    public static StringBuilder log = new StringBuilder();
    public static Date date = new Date();

    public static void main(String[] args) {
        newDir("C:\\Новая папка (2)\\Games", "Games");
        newDir("C:\\Новая папка (2)\\Games\\src", "src");
        newDir("C:\\Новая папка (2)\\Games\\res", "res");
        newDir("C:\\Новая папка (2)\\Games\\savegames", "savegames");
        newDir("C:\\Новая папка (2)\\Games\\temp", "temp");
        newDir("C:\\Новая папка (2)\\Games\\src\\main", "main");
        newDir("C:\\Новая папка (2)\\Games\\src\\test", "test");

        newFile("C:\\Новая папка (2)\\Games\\src\\main\\Main.java", "Main.java");
        newFile("C:\\Новая папка (2)\\Games\\src\\main\\Utils.java", "Utils.java");

        newDir("C:\\Новая папка (2)\\Games\\res\\drawables", "drawables");
        newDir("C:\\Новая папка (2)\\Games\\res\\vectors", "vectors");
        newDir("C:\\Новая папка (2)\\Games\\res\\icons", "icons");

        newFile("C:\\Новая папка (2)\\Games\\temp\\temp.txt", "temp.txt");

        //запись
        String text = log.toString();
        try (FileOutputStream fos = new FileOutputStream("C:\\Новая папка (2)\\Games\\temp\\temp.txt", true)) { //записать что то в выходной поток
            byte[] bytes = text.getBytes(); // получаем из строки массив байт
            fos.write(bytes, 0, bytes.length); // записать байты в файл с 0 позиц до последней
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        GameProgress user1 = new GameProgress(100, 30, 1, 200.76);
        GameProgress user2 = new GameProgress(58, 50, 30, 10000.55);
        GameProgress user3 = new GameProgress(70, 80, 40, 500000.77);

        saveGame("C:\\Новая папка (2)\\Games\\savegames\\save1.dat", user1);
        saveGame("C:\\Новая папка (2)\\Games\\savegames\\save2.dat", user2);
        saveGame("C:\\Новая папка (2)\\Games\\savegames\\save3.dat", user3);

        List<String> list = Arrays.asList("C:\\Новая папка (2)\\Games\\savegames\\save1.dat",
                "C:\\Новая папка (2)\\Games\\savegames\\save2.dat",
                "C:\\Новая папка (2)\\Games\\savegames\\save3.dat");

        zipFiles("C:\\Новая папка (2)\\Games\\savegames\\zip_output.zip", list);

        fileDelete("C:\\Новая папка (2)\\Games\\savegames\\save1.dat");
        fileDelete("C:\\Новая папка (2)\\Games\\savegames\\save2.dat");
        fileDelete("C:\\Новая папка (2)\\Games\\savegames\\save3.dat");


        openZip("C:\\Новая папка (2)\\Games\\savegames\\zip_output.zip", "C:\\Новая папка (2)\\Games\\savegames");
        System.out.println(openProgress("C:\\Новая папка (2)\\Games\\savegames\\paked_notes1.txt"));
    }

    public static void openZip(String pathZip, String pathDirectory) {
        try (ZipInputStream zin = new ZipInputStream(new FileInputStream(pathZip))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();
                FileOutputStream fout = new FileOutputStream("C:\\Новая папка (2)\\Games\\savegames\\" + name);
                for (int c = zin.read(); c != -1; c = zin.read()) {
                    fout.write(c);
                }
                fout.flush();
                zin.closeEntry();
                fout.close();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static GameProgress openProgress(String path) {
        GameProgress gameProgress = null;
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return gameProgress;
    }

    public static void fileDelete(String path) {
        File file = new File(path);
        if (file.delete()) {
            System.out.println("файл удален");
        }
    }

    public static void saveGame(String text, GameProgress user) {
        try (FileOutputStream fos = new FileOutputStream(text);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(user);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void zipFiles(String pathZip, List<String> list) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(pathZip))) { // этот файл запишем в архив

            for (int i = 0; i < list.size(); i++) {
                ZipEntry entry = new ZipEntry("paked_notes" + i + ".txt");   // записываем имя нового файла в архиве
                zout.putNextEntry(entry); // говорим что сохраняем новый файл
                FileInputStream fis = new FileInputStream(list.get(i));
                byte[] buffer = new byte[fis.available()]; // Буферт будет размера этого файла
                fis.read(buffer);
                zout.write(buffer);
                zout.closeEntry();   //  закрывает операцию
                fis.close();
            } //  закрывает файлы
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    public static void newDir(String text, String name) {
        File dir = new File(text);
        if (dir.mkdir())                          // создастся папка
            log.append("Папка " + name + " создана, время создания: " + date.toString() + "\n" + "путь к папке: " + text + "\n");
    }

    public static void newFile(String text, String name) {
        File file = new File(text); // создали файл в папке
        try {
            if (file.createNewFile())     // если есть папка до создать файл
                log.append("Файл " + name + " создан, время создания: " + date.toString() + "\n" + "путь к файлу: " + text + "\n");
        } catch (IOException e) {    // если нет папки то создать исключение
            System.out.println(e.getMessage());
        }

    }
}