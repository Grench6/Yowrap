import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//RTDN means "Relative to darknet"
public class YoloMeta
{
	public Data data;
	public Names names;
	public Train train;
	private File darknetFolder;
	private String metadataFolderRTDN;

	public YoloMeta(File darknetFolder, String metadataFolderRTDN, String imagesFolderRTDN, String[] imagesNames, String[] sortedTags)
	{
		this.darknetFolder = darknetFolder;
		this.metadataFolderRTDN = metadataFolderRTDN;
		String[] imagesRTDN = new String[imagesNames.length];
		for (int i = 0; i < imagesRTDN.length; i++)
		{
			imagesRTDN[i] = metadataFolderRTDN + "/" + imagesFolderRTDN + "/" + imagesNames[i];
		}
		train = new Train("train.txt", imagesRTDN);
		names = new Names("obj.names", sortedTags);
		data = new Data("obj.data", names.sortedTags.length, metadataFolderRTDN + "/" + train.localFileName, "none", metadataFolderRTDN + "/" + names.localFileName, "backup/");
	}

	public void createAllMetadataFiles()
	{
		Utils.writeLinesToFile(new File(darknetFolder.getAbsolutePath() + "/" + metadataFolderRTDN + "/" + data.localFileName), data.getAsStringLines(), false);
		Utils.writeLinesToFile(new File(darknetFolder.getAbsolutePath() + "/" + metadataFolderRTDN + "/" + names.localFileName), names.getAsStringLines(), false);
		Utils.writeLinesToFile(new File(darknetFolder.getAbsolutePath() + "/" + metadataFolderRTDN + "/" + train.localFileName), train.getAsStringLines(), false);
		try
		{
			Path backupPath = Paths.get(darknetFolder.getAbsolutePath() + "/" + data.backup_RTDN);
			Files.createDirectory(backupPath);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private class Data
	{
		public final String localFileName;
		private int classes;
		private String train_RTDN;
		private String valid_RTDN;
		private String names_RTDN;
		private String backup_RTDN;

		public Data(String localFileName, int classes, String train_RTDN, String valid_RTDN, String names_RTDN, String backup_RTDN)
		{
			this.localFileName = localFileName;
			this.classes = classes;
			this.train_RTDN = train_RTDN;
			this.valid_RTDN = valid_RTDN;
			this.names_RTDN = names_RTDN;
			this.backup_RTDN = backup_RTDN;
		}

		public String[] getAsStringLines()
		{
			String[] ans = new String[5];
			ans[0] = "classes = " + classes;
			ans[1] = "train = " + train_RTDN;
			ans[2] = "valid = " + valid_RTDN;
			ans[3] = "names = " + names_RTDN;
			ans[4] = "backup = " + backup_RTDN;
			return ans;
		}
	}

	private class Names
	{
		public final String localFileName;
		private final String[] sortedTags;

		public Names(String localFileName, String[] sortedTags)
		{
			this.localFileName = localFileName;
			this.sortedTags = sortedTags;
		}

		public String[] getAsStringLines()
		{
			return sortedTags;
		}
	}

	private class Train
	{
		public final String localFileName;
		private final String[] imagesRTDN;

		public Train(String localFileName, String[] imagesRTDN)
		{
			this.localFileName = localFileName;
			this.imagesRTDN = imagesRTDN;
		}

		public String[] getAsStringLines()
		{
			return imagesRTDN;
		}
	}
}
