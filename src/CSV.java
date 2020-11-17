import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class CSV
{
	public CSVEntry[] entries;
	public String[] tags;
	public String[] imageNames;

	private CSV(CSVEntry[] entries, String[] tags, String[] imageNames)
	{
		this.entries = entries;
		this.tags = tags;
		this.imageNames = imageNames;
	}

	public static CSV readCSVFromFile(File file)
	{
		ArrayList<CSVEntry> tempEntries = new ArrayList<CSVEntry>();
		ArrayList<String> tempTags = new ArrayList<String>();
		ArrayList<String> tempImageNames = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(file)))
		{
			String line = br.readLine(); // Ignore first line
			while ((line = br.readLine()) != null)
			{
				String fileName = Utils.extractExclusive(line.substring(1), "\",");
				String xmin = Utils.removeInclusive(line, ",");
				String ymin = Utils.removeInclusive(xmin, ",");
				String xmax = Utils.removeInclusive(ymin, ",");
				String ymax = Utils.removeInclusive(xmax, ",");
				String tag = Utils.removeInclusive(ymax, ",\"");
				tag = Utils.extractExclusive(tag, "\"");
				xmin = Utils.extractExclusive(xmin, ",");
				ymin = Utils.extractExclusive(ymin, ",");
				xmax = Utils.extractExclusive(xmax, ",");
				ymax = Utils.extractExclusive(ymax, ",");
				CSVEntry temp_CSVEntry = new CSVEntry(fileName, Double.parseDouble(xmin), Double.parseDouble(ymin), Double.parseDouble(xmax), Double.parseDouble(ymax), tag);
				if (!tempTags.contains(tag))
					tempTags.add(tag);
				if (!tempImageNames.contains(fileName))
					tempImageNames.add(fileName);
				tempEntries.add(temp_CSVEntry);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		CSVEntry[] ans_tempEntries = new CSVEntry[tempEntries.size()];
		ans_tempEntries = tempEntries.toArray(ans_tempEntries);

		String[] ans_tempTags = new String[tempTags.size()];
		ans_tempTags = tempTags.toArray(ans_tempTags);

		String[] ans_tempImageNames = new String[tempImageNames.size()];
		ans_tempImageNames = tempImageNames.toArray(ans_tempImageNames);

		return new CSV(ans_tempEntries, ans_tempTags, ans_tempImageNames);
	}

	public String[] getAsStringLines()
	{
		String[] ans = new String[entries.length];
		for (int i = 0; i < ans.length; i++)
		{
			ans[i] = entries[i].toString();
		}
		return ans;
	}
}
