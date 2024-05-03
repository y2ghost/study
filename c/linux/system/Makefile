ROOT := .
DIRS := lib intro sockets advio environ \
	termios fileio filedir signals user time filesys \
	dirs timers procexec cap pipes ipc

.PHONY: all clean

all:
	for d in $(DIRS); do \
		(cd $$d && echo "making $$d" && $(MAKE) ) || exit 1; \
	done

clean:
	for d in $(DIRS); do \
		(cd $$d && echo "cleaning $$d" && $(MAKE) clean) || exit 1; \
	done

