
public class Yolo
{
	public String localName;
	public YoloEntry[] entries;

	public Yolo(String localName, YoloEntry[] entries)
	{
		this.localName = localName;
		this.entries = entries;
	}

	public Yolo(String localName, YoloEntry entry)
	{
		this.localName = localName;
		this.entries = new YoloEntry[1];
		this.entries[0] = entry;
	}

	public void addEntry(YoloEntry newEntry)
	{
		YoloEntry[] newEntries = new YoloEntry[entries.length + 1];
		for (int i = 0; i < entries.length; i++)
		{
			newEntries[i] = entries[i];
		}
		newEntries[entries.length] = newEntry;
		entries = newEntries;
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
