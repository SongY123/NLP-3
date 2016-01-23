v1.0

介绍

本说明文件来自如下网址:
http://nlp.csai.tsinghua.edu.cn/~lj/ .



引用信息

这个数据集在下面论文中首次使用:
Jun Li and Maosong Sun, Experimental Study on Sentiment Classification of Chinese Review using Machine Learning Techniques, in Procedings of IEEE International Conference on Natural Language Processing and Knowledge Engineering, 2007 

@INPROCEEDINGS{Li2007,
  author = {Li, Jun and Sun, Maosong},
  title = {Experimental Study on Sentiment Classification of Chinese Review using Machine Learning Techniques},
  booktitle = {Procedings of IEEE International Conference on Natural Language Processing and Knowledge Engineering},
  year = {2007},
}



语料库描述: (摘自论文)

All we gathered our data from www.ctrip.com (an online consolidator in China). The customers can write reviews on the hotels which they stayed. Along with the review they provide a numeric satisfaction score on a scale from 1 (discontented) to 5 (satisfied).
The corpus was obtained as follows. First we use a web crawler to collect all the reviews from the website on March 17, 2007. After these docu-ments were cleaned to remove html tags and parsed as plain text with corresponding score, we then assume that reviews marked with 4.5 and up are considered positive, 2.0 and below are consid-ered negative. 18,158 positive and 8,317 negative reviews are acquired. After that we randomly se-lected 6,000 reviews as training set, 2,000 reviews as test set from both categories respectively. That is to say, we got 12,000 reviews as training set, 4,000 reviews as test set in the end.



数据格式说明:

train2/       -- 包含训练用评论文件的目录. (每个文件是一个评论)
train2.list   -- 训练用评论文件名列表
train2.rlabelclass  -- 训练用文件名及其标签
test2/        -- 包含测试用评论文件的目录. (每个文件是一个评论)
test2.list    -- 测试用用评论文件名列表
test2.rlabelclass  -- 测试用文件名及其标签

对于标签, +1 表示褒义, -1表示贬义.

你可以使用 TextMatrix (http://nlp.csai.tsinghua.edu.cn/~lj/) 抽取特征,生成分类器所需的文件格式.



数据集上的分类性能:

见发表论文.
