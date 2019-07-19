package com.wh.hadoop.flow.sum;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.conf.Configuration;

public class FlowSumRunner extends Configured implements Tool {

	@Override
	public int run(String[] arg0) throws Exception {
		Configuration config = new Configuration();
		
		Job  job = Job.getInstance(config);
		job.setJarByClass(FlowSumRunner.class);
		
		job.setMapperClass(FlowSumMapper.class);
		job.setReducerClass(FlowSumReducer.class);
		
		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(FlowBean.class);
		
		FileInputFormat.setInputPaths(job,new Path(arg0[0]));
		FileOutputFormat.setOutputPath(job, new Path(arg0[1]));
		return job.waitForCompletion(true)? 0:1;
	}
	public static void main(String[] args) throws Exception {
		int flag = ToolRunner.run(new Configuration(), new FlowSumRunner(), args);
		System.out.println(flag);
	}

}
