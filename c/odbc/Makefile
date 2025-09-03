odbc_test: odbc_sample.c
	cc -Wall -Werror -g -O2 -I$(HOME)/progs/include \
	-o $@ $^ -L$(HOME)/progs -lodbc 

.PHONY: clean
clean:
	rm -f odbc_test
