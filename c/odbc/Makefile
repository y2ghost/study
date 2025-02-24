odbc_test: odbc_sample.c
	cc -Wall -Werror -g -O2 -I/home/yy/progs/include \
	-o $@ $^ -L/home/yy/progs -lodbc 

.PHONY: clean
clean:
	rm -f odbc_test
