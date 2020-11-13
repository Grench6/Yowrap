
public class Yolo
{
	public String localName;

	public int tagIndex;
	public float midX;
	public float midY;
	public float width;
	public float height;

	public Yolo(String localName, int tagIndex, float midX, float midY, float width, float height)
	{
		this.localName = localName;
		this.tagIndex = tagIndex;
		this.midX = midX;
		this.midY = midY;
		this.width = width;
		this.height = height;
	}

	public void print()
	{
		System.out.println(localName + "; " + getAsString());
	}

	public String getAsString()
	{
		return tagIndex + " " + midX + " " + midY + " " + width + " " + height;
	}
}
