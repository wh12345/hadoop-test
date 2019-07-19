package com.wh.hadoop.flow.sum;

import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class SortMR {
	
	public static class SortMapper extends Mapper<LongWritable, Text, FlowBean, NullWritable> {

		@Override
		protected void map(LongWritable key, Text value,
		                    Mapper<LongWritable, Text, FlowBean, NullWritable>.Context context)
		                    throws IOException, InterruptedException {
			String line = value .toString();
			String[] fields = StringUtils.split(value.toString(), "\t");
			
			String phoneNB =  fields[0].trim();
			long u_flow = Long.parseLong(fields[1].trim());
			long d_flow = Long.parseLong(fields[2].trim());
			context.write(new FlowBean(phoneNB, u_flow, d_flow),NullWritable.get());
		}
		
	}
	
	public static class SortReducer extends Reducer<FlowBean, NullWritable, Text, FlowBean>  {

		@Override
		protected void reduce(FlowBean flowBean, Iterable<NullWritable> arg1,Reducer<FlowBean, NullWritable, Text, FlowBean>.Context context) throws IOException, InterruptedException {
			context.write(new Text(flowBean.getPhoneNB()), flowBean);
		}

		

	
		
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
		Configuration config = new Configuration();
		
		Job  job = Job.getInstance(config);
		job.setJarByClass(SortMR.class);
		
		job.setMapperClass(SortMapper.class);
		job.setReducerClass(SortReducer.class);
		
		job.setOutputKeyClass(FlowBean.class);
		job.setOutputValueClass(NullWritable.class);
		
		FileInputFormat.setInputPaths(job,new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));
		job.waitForCompletion(true);
	}

}
