import java.awt.Dimension;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Wrapper
{
	private final boolean WRITE_YOLOS = true;
	private final boolean renameToLinear;
	private final boolean copyImages;
	private final boolean generateMeta;
	private final File folder_read;
	private final File folder_darknet;
	private final File folder_imagesAndYolo;
	private final String metaFolderName;
	private final String imagesAndYoloFolderName;

	public Wrapper(File folder_read, File folder_darknet, String metaFolderName, String imagesAndYoloFolderName, boolean copyImages, boolean renameToLinear)
	{
		this.renameToLinear = renameToLinear;
		this.folder_read = folder_read;
		this.copyImages = copyImages;
		this.metaFolderName = metaFolderName;
		this.imagesAndYoloFolderName = imagesAndYoloFolderName;
		this.generateMeta = metaFolderName != null && metaFolderName != null;
		if (generateMeta)
		{
			this.folder_darknet = folder_darknet;
			this.folder_imagesAndYolo = new File(folder_darknet.getAbsolutePath() + "/" + metaFolderName + "/" + imagesAndYoloFolderName);
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
		Yolo[] yolos = csvToYolos(csv);
		String[] imageNames = csv.imageNames.clone();

		if (renameToLinear)
			for (int i = 0; i < imageNames.length; i++)
				imageNames[i] = i + "." + Utils.inverseExtractExclusive(imageNames[i], ".");

		assert yolos.length == imageNames.length;

		for (int i = 0; i < yolos.length; i++)
		{
			if (WRITE_YOLOS)
			{
				File yoloFile = new File(folder_imagesAndYolo.getAbsolutePath() + "/" + Utils.inverseRemoveInclusive(imageNames[i], ".") + ".txt");
				Utils.writeLinesToFile(yoloFile, yolos[i].getAsStringLines(), false);
			}
			if (copyImages)
			{
				File source = new File(folder_read.getAbsolutePath() + "/" + csv.imageNames[i]);
				File destination = new File(folder_imagesAndYolo.getAbsolutePath() + "/" + imageNames[i]);
				Utils.copyFile(source, destination);
			}
		}

		if (generateMeta)
		{
			YoloMeta yoloMeta = new YoloMeta(folder_darknet, metaFolderName, imagesAndYoloFolderName, imageNames, csv.tags);
			yoloMeta.createAllMetadataFiles();
		}
	}

	private Yolo[] csvToYolos(CSV csv)
	{
		ArrayList<Yolo> yolos = new ArrayList<Yolo>();
		for (int i = 0; i < csv.entries.length; i++)
		{
			CSVEntry currentCSVEntry = csv.entries[i];
			String localName = Utils.inverseRemoveInclusive(currentCSVEntry.localName, ".") + ".txt";
			YoloEntry currentEntry = csvEntryToYoloEntry(currentCSVEntry, csv.tags);

			boolean found = false;
			for (Yolo current : yolos)
			{
				if (current.localName.compareTo(localName) == 0)
				{
					current.addEntry(currentEntry);
					found = true;
					break;
				}
			}
			if (!found)
			{
				Yolo newYolo = new Yolo(localName, currentEntry);
				yolos.add(newYolo);
			}
		}
		Yolo[] ans_yolo = new Yolo[yolos.size()];
		ans_yolo = yolos.toArray(ans_yolo);
		return ans_yolo;
	}

	private YoloEntry csvEntryToYoloEntry(CSVEntry entry, String[] indexedTags)
	{
		Dimension dimensions = Utils.getDimensionsOfImage(new File(folder_read.getAbsolutePath() + "/" + entry.localName));
		double width = entry.xmax - entry.xmin;
		double height = entry.ymax - entry.ymin;
		double midX = entry.xmin + (width / ((double) 2));
		double midY = entry.ymin + (height / ((double) 2));
		width /= ((double) dimensions.width);
		height /= ((double) dimensions.height);
		midX /= ((double) dimensions.width);
		midY /= ((double) dimensions.height);
		int tagIndex = -1;
		for (int i = 0; i < indexedTags.length; i++)
		{
			if (indexedTags[i].compareTo(entry.tag) == 0)
			{
				tagIndex = i;
				break;
			}
		}
		YoloEntry yoloEntry = new YoloEntry(tagIndex, (float) midX, (float) midY, (float) width, (float) height);
		return yoloEntry;
	}
}