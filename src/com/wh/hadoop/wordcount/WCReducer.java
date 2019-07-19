package com.wh.hadoop.wordcount;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class WCReducer extends Reducer<Text, LongWritable, Text,LongWritable>{

	@Override
	protected void reduce(Text text, Iterable<LongWritable> values,Reducer<Text, LongWritable, Text, LongWritable>.Context context)throws IOException, InterruptedException {
		long count = 0;
		for (LongWritable value : values) {
			count += value.get();
		}
		//输出这一个单词的统计结果
		context.write(text, new LongWritable(count));
		
		
	}
}
