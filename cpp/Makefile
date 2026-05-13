ROOT=.
include $(ROOT)/Make.defines

PROGS := dataTypesExample ioExample nsExample \
	functonClassExample storageExample classExample sharedPtrExample castExample \
	exceptionExample pointerExample pimplExample templateExample lambdaExample \
	copyAndSwap smartPointer rvalue

all: $(PROGS)

%: %.cpp
	$(CPP) -o $@ $(CPPFLAGS) $^

functonClassExample: functonClassExample.cpp utility.cpp vehicle.cpp consoleWriteData.cpp
	$(CPP) -o $@ $(CPPFLAGS) $^

storageExample: storageExample.cpp someClass.cpp
	$(CPP) -o $@ $(CPPFLAGS) $^ -lpthread

classExample: classExample.cpp iceCreamSundae.cpp
	$(CPP) -o $@ $(CPPFLAGS) $^

pimplExample: pimplExample.cpp sandwich.cpp
	$(CPP) -o $@ $(CPPFLAGS) $^

clean:
	rm -f $(PROGS) a.out

