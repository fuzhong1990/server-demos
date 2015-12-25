package cn.hero.ohers;

import java.io.File;
import java.io.FileFilter;

/**
 * Created by Fuzhong on 2015/12/25.
 */
public class Lambda {

    public static void main(String[] args) {
        File file = new File("D:\\Apache");
        // 没用lambda时候：
        FileFilter filter = new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        };
        File[] dirs = file.listFiles(filter);
        for (File dir : dirs) {
            System.out.println(dir.getName());
        }
        // 使用lambda后：【FileFilter directoryFilter = (File f) -> f.isDirectory();】 被简化了
        File [] dirL = file.listFiles((File f) -> f.isDirectory());
        for (File dir : dirL) {
            System.out.println(dir.getName());
        }
    }

}
