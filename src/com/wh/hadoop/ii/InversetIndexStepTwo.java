package com.wh.hadoop.ii;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InversetIndexStepTwo {

	public static class  StepTwoMapper extends Mapper<LongWritable,Text, Text, Text> {

		@Override
		protected void map(LongWritable key,Text value, Mapper<LongWritable, Text,Text, Text>.Context context)throws IOException, InterruptedException {
			String line =value.toString();
			String[] fields = StringUtils.split(line,"\t");
			String[] wordandfilename =StringUtils.split(fields[0],"-->");
			String word = wordandfilename[0];
			String filename  = wordandfilename[1];
			long count = Long.parseLong(fields[1]);
			context.write(new Text(word), new Text(filename+"-->"+count));
		}
		
	}
	
	public static class StepTwoReducer extends Reducer<Text, Text, Text, Text> {

		@Override
		protected void reduce(Text text, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context)throws IOException, InterruptedException {
			String result = "";	
			for (Text value : values) {
				result += value+ " ";
			}
			context.write(text, new Text(result));
		}
		
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = new Configuration();
		
		Job  job = Job.getInstance(config);
		job.setJarByClass(InversetIndexStepTwo.class);
		
		job.setMapperClass(StepTwoMapper.class);
		job.setReducerClass(StepTwoReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);
		
		FileInputFormat.setInputPaths(job,new Path(args[0]));
		Path out  =  new Path(args[1]);
		FileSystem fs  = FileSystem.get(config);
		if (fs.exists(out)) {
			fs.delete(out,true);
		}
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}
}
