Introduction

This README v1.0 (July, 2007) for the v1.0 review sentiment comes from the URL http://nlp.csai.tsinghua.edu.cn/~lj/ .



Citation Info

This data was first used in 
Jun Li and Maosong Sun, Experimental Study on Sentiment Classification of Chinese Review using Machine Learning Techniques, in Procedings of IEEE International Conference on Natural Language Processing and Knowledge Engineering, 2007 

@INPROCEEDINGS{Li2007,
  author = {Li, Jun and Sun, Maosong},
  title = {Experimental Study on Sentiment Classification of Chinese Review using Machine Learning Techniques},
  booktitle = {Procedings of IEEE International Conference on Natural Language Processing and Knowledge Engineering},
  year = {2007},
}



Corpus Description (from paper)

All we gathered our data from www.ctrip.com (an online consolidator in China). The customers can write reviews on the hotels which they stayed. Along with the review they provide a numeric satisfaction score on a scale from 1 (discontented) to 5 (satisfied).
The corpus was obtained as follows. First we use a web crawler to collect all the reviews from the website on March 17, 2007. After these docu-ments were cleaned to remove html tags and parsed as plain text with corresponding score, we then assume that reviews marked with 4.5 and up are considered positive, 2.0 and below are consid-ered negative. 18,158 positive and 8,317 negative reviews are acquired. After that we randomly se-lected 6,000 reviews as training set, 2,000 reviews as test set from both categories respectively. That is to say, we got 12,000 reviews as training set, 4,000 reviews as test set in the end.



Data Format Summary 

train2/       -- Directory which contains all the training review. (one review per text file)
train2.list   -- Review file list of training.
train2.rlabelclass  -- Review file list of training with polarity label 
test2/        -- Directory which contains all the testing review. (one review per text file)
test2.list    -- Review file list of testing.
test2.rlabelclass  -- Review file list of testing with polarity label 

For polarity label, +1 means positive, -1 means negative.

You can use TextMatrix (http://nlp.csai.tsinghua.edu.cn/~lj/) to extract features.



Performance on data sets

See my paper.
