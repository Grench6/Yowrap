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

	public YoloMeta(File darknetFolder, String metadataFolderRTDN, String imagesFolderRTDN, String[] imagesNames,
			String[] sortedTags)
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
		data = new Data("obj.data", names.sortedTags.length, train.localFileName, "none", names.localFileName,
				"backup/");
	}

	public void createAllMetadataFiles()
	{
		Utils.writeLinesToFile(
				new File(darknetFolder.getAbsolutePath() + "/" + metadataFolderRTDN + "/" + data.localFileName),
				data.getAsStringLines());
		Utils.writeLinesToFile(
				new File(darknetFolder.getAbsolutePath() + "/" + metadataFolderRTDN + "/" + names.localFileName),
				names.getAsStringLines());
		Utils.writeLinesToFile(
				new File(darknetFolder.getAbsolutePath() + "/" + metadataFolderRTDN + "/" + train.localFileName),
				train.getAsStringLines());
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
		private String train_localFileName;
		private String valid_localFileName;
		private String names_localFileName;
		private String backup_RTDN;

		public Data(String localFileName, int classes, String train_localFileName, String valid_localFileName,
				String names_localFileName, String backup_RTDN)
		{
			this.localFileName = localFileName;
			this.classes = classes;
			this.train_localFileName = train_localFileName;
			this.valid_localFileName = valid_localFileName;
			this.names_localFileName = names_localFileName;
			this.backup_RTDN = backup_RTDN;
		}

		public String[] getAsStringLines()
		{
			String[] ans = new String[5];
			ans[0] = "classes = " + classes;
			ans[1] = "train = " + train_localFileName;
			ans[2] = "valid = " + valid_localFileName;
			ans[3] = "names = " + names_localFileName;
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
