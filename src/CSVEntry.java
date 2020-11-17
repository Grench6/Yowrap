
public class CSVEntry
{
	public String localName;
	public double xmin;
	public double ymin;
	public double xmax;
	public double ymax;
	public String tag;

	public CSVEntry(String localName, double xmin, double ymin, double xmax, double ymax, String tag)
	{
		this.localName = localName;
		this.xmin = xmin;
		this.ymin = ymin;
		this.xmax = xmax;
		this.ymax = ymax;
		this.tag = tag;
	}

	@Override
	public String toString()
	{
		return localName + "," + xmin + "," + ymin + "," + xmax + "," + ymax + "," + tag;
	}
}
