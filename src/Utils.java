
import java.awt.Dimension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

public class Utils
{
	public static String removeInclusive(String original, String pattern)
	{
		int index = original.indexOf(pattern);
		if (index == -1)
		{
			return original;
		}
		return original.substring(index + pattern.length(), original.length());
	}

	public static String extractExclusive(String original, String pattern)
	{
		int index = original.indexOf(pattern);
		if (index == -1)
		{
			return original;
		}
		return original.substring(0, index);
	}

	public static String inverseRemoveInclusive(String original, String pattern)
	{
		int index = original.lastIndexOf(pattern);
		if (index == -1)
		{
			return original;
		}
		return original.substring(0, index);
	}

	public static String inverseExtractExclusive(String original, String pattern)
	{
		int index = original.lastIndexOf(pattern);
		if (index == -1)
		{
			return original;
		}
		return original.substring(index + pattern.length(), original.length());
	}

	public static Dimension getDimensionsOfImage(File file)
	{
		try (ImageInputStream in = ImageIO.createImageInputStream(file))
		{
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(in);
			if (readers.hasNext())
			{
				ImageReader reader = readers.next();
				try
				{
					reader.setInput(in);
					return new Dimension(reader.getWidth(0), reader.getHeight(0));
				} finally
				{
					reader.dispose();
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	public static void writeLinesToFile(File file, String[] lines, boolean append)
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append)))
		{
			for (int i = 0; i < lines.length; i++)
			{
				if (i != 0)
				{
					// Since I use the wrapper in Windows, but I use its output in Unix...
					bw.write('\n');
					// bw.newLine();
				}
				bw.write(lines[i]);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void writeLineToFile(File file, String line, boolean append)
	{
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append)))
		{
			if (append)
			{
				// Since I use the wrapper in Windows, but I use its output in Unix...
				bw.write('\n');
				// bw.newLine();
			}
			bw.write(line);
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void copyFile(File src, File dst)
	{
		try (InputStream is = new FileInputStream(src); OutputStream os = new FileOutputStream(dst))
		{
			byte[] buffer = new byte[1024];
			int length;
			while ((length = is.read(buffer)) > 0)
			{
				os.write(buffer, 0, length);
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static File getExecutionPath()
	{
		String dir = System.getProperty("user.dir");
		return new File(dir);
	}
}
