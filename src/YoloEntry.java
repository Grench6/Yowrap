
public class YoloEntry
{
	public int tagIndex;
	public float midX;
	public float midY;
	public float width;
	public float height;

	public YoloEntry(int tagIndex, float midX, float midY, float width, float height)
	{
		this.tagIndex = tagIndex;
		this.midX = midX;
		this.midY = midY;
		this.width = width;
		this.height = height;
	}

	@Override
	public String toString()
	{
		return tagIndex + " " + midX + " " + midY + " " + width + " " + height;
	}
}
