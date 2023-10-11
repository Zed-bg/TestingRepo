import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class EPIXEqualDiff {

	public static void main(String[] args) {
		try {
			File file1 = new File("F:\\EPIXEqualDiff\\appservice.txt");

			BufferedReader br1 = new BufferedReader(new FileReader(file1));

			String st1;

			ArrayList<String> appservicearray = new ArrayList<String>();

			while ((st1 = br1.readLine()) != null) {
				// System.out.println(st1);
				// if(!st1.equals(""))
				appservicearray.add(st1);
			}

			File file2 = new File("F:\\EPIXEqualDiff\\appservice.txt");

			BufferedReader br2 = new BufferedReader(new FileReader(file2));

			String st2;

			ArrayList<String> epixwebserviceearray = new ArrayList<String>();

			while ((st2 = br2.readLine()) != null) {
				// System.out.println(st2);
				// if(!st2.equals(""))
				epixwebserviceearray.add(st2);
			}
			Collections.sort(appservicearray);
			Collections.sort(epixwebserviceearray);

			FileWriter fw1 = new FileWriter("F:\\EPIXEqualDiff\\mine.txt");

			for (String str3 : appservicearray) {
				fw1.write("\r\n");
				fw1.write(str3);
			}

			fw1.close();

			FileWriter fw2 = new FileWriter("F:\\EPIXEqualDiff\\phk.txt");

			for (String str4 : epixwebserviceearray) {
				fw2.write("\r\n");
				fw2.write(str4);
			}

			fw2.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}