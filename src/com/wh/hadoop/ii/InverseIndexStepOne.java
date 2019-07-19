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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class InverseIndexStepOne {

	public  static class StepMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
		@Override
		protected void map(LongWritable key, Text value,Mapper<LongWritable, Text, Text, LongWritable>.Context context)throws IOException, InterruptedException {
		         String line = value.toString();
		         String[] fields =StringUtils.split(line," ");
		         FileSplit fileSplit  = (FileSplit)context.getInputSplit();
		         String fileName  = fileSplit.getPath().getName();
		         for (String field : fields) {
			         context.write(new Text(field+"-->"+fileName), new LongWritable(1));
		         }
		}
		
	}
	
	public static class StepReducer extends Reducer<Text,LongWritable, Text, LongWritable> {

		@Override
		protected void reduce(Text text, Iterable<LongWritable> values,Reducer<Text, LongWritable, Text, LongWritable>.Context context)
		                    throws IOException, InterruptedException {
			long counter =0;
			for (LongWritable value : values) {
				counter +=value.get();
			}
			context.write(text, new LongWritable(counter));
		}
		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = new Configuration();
		
		Job  job = Job.getInstance(config);
		job.setJarByClass(InverseIndexStepOne.class);
		
		job.setMapperClass(StepMapper.class);
		job.setReducerClass(StepReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(LongWritable.class);
		
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
