These code, graphs (see report) and report were created in 2020 to approach 2 tasks in NLP.
The tasks in general (more details are omitted here) were as follows:

**Task 1**: 

implement Deep Learning models and a full pipeline (from reading data for
		training till evaluation of the trained models) to perform Slot Filling and
		Intent Classification for the given data; use 2 different types of pre-trained
		word embeddings, compare results.

**Task 2**: 

perform token classification in a very low-resource scenario: 
		a small amount of annotated in-domain training examples are available
		together with a bigger number of unannotated in-domain utterances,
		plus some annotated and unannotated data from several other domains can be used.
		(A classical Machine Learning method combined with word embeddings was used.) 

Data 

  Task1: train/dev/test sets in the format:
  - one utterance per line;
  - each example in the form:

			token1:<slot> token2:<slot>... tokenN:<slot> <=> <intent>

  Task2: annotated and unannotated in- and out-of-domain train data plus annotated test
		data, in the format:
  - one utterance per line;
  - annotated utterances in the the form:

			token1||<slot> token2||<slot>... tokenN||<slot><=><domain>
