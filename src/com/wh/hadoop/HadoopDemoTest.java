package com.wh.hadoop;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Before;
import org.junit.Test;

public class HadoopDemoTest {
	private FileSystem fs;
	@Before
	public  void init() throws IOException, InterruptedException, URISyntaxException {
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS","hdfs://weekend110:9000");
	          fs = FileSystem.get(new URI("hdfs://weekend110:9000"), conf, "hadoop");
	}
	@Test
	public void  upload() throws IOException {		
		Path path  = new Path("hdfs://weekend110:9000/wordcount/input/123.txt");		
		FSDataOutputStream os = fs.create(path);
		FileInputStream is = new FileInputStream("G:\\test\\123.txt");
	          IOUtils.copy(is, os);
	}
	@Test
	public void download() throws IOException {
		Path path  = new Path("hdfs://weekend110:9000/wordcount/input/123.txt");	
		Path path2 = new Path("G:\\test\\123replication.txt");
		fs.copyToLocalFile(path, path2);
	}

	@Test
	public void mkdir() throws IOException {
		Path path  = new Path("hdfs://weekend110:9000/aa");	
		fs.mkdirs(path);
	}
	@Test
	public void listdir() throws FileNotFoundException, IOException {
		Path path  = new Path("hdfs://weekend110:9000/wordcount");
		RemoteIterator<LocatedFileStatus>  status = fs.listFiles(path, true);
		while(status.hasNext()) {
			LocatedFileStatus fileStatus = status.next();
			Path p = fileStatus.getPath();
			System.out.println(p.getName());
		}
	}
	@Test
	public void deleteFile() throws IOException {
		Path path  = new Path("hdfs://weekend110:9000/aa");	
		fs.delete(path);
	}
}
