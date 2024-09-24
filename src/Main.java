import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class Main {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
//  создаем директории src, res, savegames и temp в папке Games
        ArrayList<String> foldersInGames = new ArrayList<>();
        File[] listFiles = null;
        foldersInGames.add("src");
        foldersInGames.add("res");
        foldersInGames.add("savegames");
        foldersInGames.add("temp");
        for (String folder : foldersInGames) {
            File dir = foldersCreator("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\", folder, sb);
            System.out.println("Каталог " + folder + " создан.");
        }
//  создаем директории main и test в каталоге src
        ArrayList<String> foldersInSrc = new ArrayList<>();
        foldersInSrc.add("main");
        foldersInSrc.add("test");
        for (String folder : foldersInSrc) {
            File dir = foldersCreator("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\src\\", folder, sb);
            System.out.println("Подкаталог " + folder + " создан.");
        }
//  создаем файлы Main.java и Utils.java в каталоге main
        ArrayList<String> filesInMain = new ArrayList<>();
        filesInMain.add("Main.java");
        filesInMain.add("Utils.java");
        for (String file : filesInMain) {
            File dir = filesCreator("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\src\\main\\", file, sb);
            System.out.println("Файл " + file + " создан.");
        }
//  создаем директории drawables, vectors, icons в каталоге res
        ArrayList<String> foldersInRes = new ArrayList<>();
        foldersInRes.add("drawables");
        foldersInRes.add("vectors");
        foldersInRes.add("icons");
        for (String folder : foldersInRes) {
            File dir = foldersCreator("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\res\\", folder, sb);
            System.out.println("Каталог " + folder + " создан.");
        }
//  создаем файл temp.txt в каталоге temp
        String fileInTemp = "temp.txt";
        File file = filesCreator("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\temp\\", fileInTemp, sb);
        System.out.println("Файл " + fileInTemp + " создан.");

//  записываем в temp.txt информацию об успешноном или неуспешном создании файлов и директорий
        writerInFile("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\temp\\", "temp.txt", sb);

//  создаем 3 экземпляра класса сохраненной игры
        GameProgress gameProgress1 = new GameProgress(94, 10, 2, 254.32);
        GameProgress gameProgress2 = new GameProgress(84, 5, 3, 274.52);
        GameProgress gameProgress3 = new GameProgress(54, 1, 5, 354.32);

//  открываем выходной поток для записи в файл
        saveGame("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\save1.dat", gameProgress1);
        saveGame("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\save2.dat", gameProgress2);
        saveGame("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\save3.dat", gameProgress3);

//  создаем список файлов
        File dirSavegames = new File("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames");
        List<File> inputFiles = new ArrayList<>(Arrays.asList(Objects.requireNonNull(dirSavegames.listFiles())));
        System.out.println(inputFiles);

//  архивируем содержимое папки savegames
        zipFiles("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\zip.zip", inputFiles);
//  удаляем файлы сохранений, не лежащие в архиве.
        if (dirSavegames.isDirectory()) {
//  получаем все вложенные объекты в каталоге
            for (File item : Objects.requireNonNull(dirSavegames.listFiles())) {
//  проверяем, является ли объект архивом
                if (item.getName().equals("zip.zip")) {
                    System.out.println(item.getName() + " - Архивированный каталог");
                } else {
                    System.out.println("Файл " + item.getName() + " - удален ");
                    item.delete();
                }
            }
       }
// производим распаковку архива в папке savegames
        openZip("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\zip.zip", dirSavegames.getName());
// проверяем содержимое папки
        if (dirSavegames.isDirectory()) {
// получаем все вложенные объекты в каталоге
            for (File item : Objects.requireNonNull(dirSavegames.listFiles())) {
// проверяем, является ли объект каталогом
                if (item.isDirectory()) {
                    System.out.println(item.getName() + " - каталог");
                } else {
                    System.out.println(item.getName() + " - файл");
                }
            }
        }
// производим считывание и десериализацию разархивированного файла save1.dat
        openProgress("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\save1.dat");
// производим считывание и десериализацию разархивированного файла save2.dat
        openProgress("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\save2.dat");
// производим считывание и десериализацию разархивированного файла save3.dat
        openProgress("C:\\Users\\Ольга\\IdeaProjects\\java core\\1.3files-directories\\Games\\savegames\\save3.dat");
    }

    public static File foldersCreator(String dirPath, String folder, StringBuilder stringBuilder) {
        File dir = new File(String.valueOf(dirPath) + String.valueOf(folder));
        if (!dir.exists()) {
            dir.mkdir();
            stringBuilder.append("Каталог ").append(folder).append(" создан успешно!\n");
        }
        return dir;
    }

    public static File filesCreator(String dirPath, String nameOfFile, StringBuilder stringBuilder) {
        File file = new File(String.valueOf(dirPath) + String.valueOf(nameOfFile));
        try {
            if (file.createNewFile())
                stringBuilder.append("Файл ").append(nameOfFile).append(" создан успешно!\n");
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return file;
    }

    public static void writerInFile(String dirPath, String nameOfFile, StringBuilder stringBuilder) {
        String report = stringBuilder.toString();
        try (FileWriter writer = new FileWriter(String.valueOf(dirPath) + String.valueOf(nameOfFile))) {
            writer.write(report);
// дозаписываем и очищаем буфер
            writer.flush();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void saveGame(String path, GameProgress gameProgress) {
        try (FileOutputStream fos = new FileOutputStream(path);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
// записываем экземпляр класса в файл
            oos.writeObject(gameProgress);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void openProgress(String path) {
        GameProgress gameProgress = null;
// открываем входной поток для чтения файла
        try (FileInputStream fis = new FileInputStream(path);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
// десериализуем объект и скастим его в класс
            gameProgress = (GameProgress) ois.readObject();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        System.out.println(gameProgress);
    }

    public static void zipFiles(String zipPath, List<File> filesToZip) {
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipPath))) {
// получаем все вложенные объекты в каталоге
            for (File file : filesToZip) {
                try (FileInputStream fis = new FileInputStream(file)) {
                    zout.putNextEntry(new ZipEntry(file.getName()));
// считываем содержимое файла в массив byte
                    byte[] buffer = new byte[fis.available()];
                    int count;
                    while ((count = fis.read(buffer)) != -1) {
                        zout.write(buffer, 0, count);
                    }
                    zout.closeEntry();
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static void openZip(String zipPath, String dirPath) {
        try (ZipInputStream zin = new ZipInputStream(new
                FileInputStream(zipPath))) {
            ZipEntry entry;
            String name;
            while ((entry = zin.getNextEntry()) != null) {
                name = entry.getName();// получим название файла
// распаковка
                FileOutputStream fout = new FileOutputStream(name);
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
}
