import java.awt.Dimension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Wrapper
{
	private final boolean copyImages;
	private final boolean generateMeta;
	private final File folder_read;
	private final File folder_darknet;
	private final File folder_imagesAndYolo;
	private final String metaFolderName;
	private final String imagesAndYoloFolderName;

	public Wrapper(File folder_read, File folder_darknet, String metaFolderName, String imagesAndYoloFolderName,
			boolean copyImages)
	{
		this.folder_read = folder_read;
		this.copyImages = copyImages;
		this.metaFolderName = metaFolderName;
		this.imagesAndYoloFolderName = imagesAndYoloFolderName;
		this.generateMeta = metaFolderName != null && metaFolderName != null;
		if (generateMeta)
		{
			this.folder_darknet = folder_darknet;
			this.folder_imagesAndYolo = new File(
					folder_darknet.getAbsolutePath() + "/" + metaFolderName + "/" + imagesAndYoloFolderName);
		} else
		{
			this.folder_darknet = null;
			this.folder_imagesAndYolo = folder_darknet;
		}
		Path deepestPath = Paths.get(folder_imagesAndYolo.getAbsolutePath());
		try
		{
			Files.createDirectories(deepestPath);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public void wrap(CSV csv)
	{
		Yolo[] yolos = new Yolo[csv.entries.length];
		for (int i = 0; i < yolos.length; i++)
		{
			CSVEntry currentEntry = csv.entries[i];
			yolos[i] = csvEntryToYolo(csv, currentEntry);
			if (copyImages)
			{
				File source = new File(folder_read.getAbsolutePath() + "/" + currentEntry.localName);
				File destination = new File(folder_imagesAndYolo.getAbsolutePath() + "/" + currentEntry.localName);
				Utils.copyFile(source, destination);
			}
		}
		if (generateMeta)
		{
			YoloMeta yoloMeta = new YoloMeta(folder_darknet, metaFolderName, imagesAndYoloFolderName, csv.imageNames,
					csv.tags);
			yoloMeta.createAllMetadataFiles();
		}
	}

	private Yolo csvEntryToYolo(CSV master, CSVEntry entry)
	{
		Dimension dimensions = Utils
				.getDimensionsOfImage(new File(folder_read.getAbsolutePath() + "/" + entry.localName));
		String localName = Utils.inverseRemoveInclusive(entry.localName, ".") + ".txt";
		double width = entry.xmax - entry.xmin;
		double height = entry.ymax - entry.ymin;
		double midX = entry.xmin + (width / ((double) 2));
		double midY = entry.ymin + (height / ((double) 2));
		width /= ((double) dimensions.width);
		height /= ((double) dimensions.height);
		midX /= ((double) dimensions.width);
		midY /= ((double) dimensions.height);
		int tagIndex = -1;
		for (int i = 0; i < master.tags.length; i++)
		{
			if (master.tags[i].compareTo(entry.tag) == 0)
			{
				tagIndex = i;
				break;
			}
		}
		Yolo yolo = new Yolo(localName, tagIndex, (float) midX, (float) midY, (float) width, (float) height);
		Utils.writeLineToFile(new File(folder_imagesAndYolo.getAbsolutePath() + "/" + yolo.localName),
				yolo.getAsString());
		return yolo;
	}
}