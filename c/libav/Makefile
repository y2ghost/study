clean:
	@rm -rf ./build/hello
	@rm -rf ./build/remuxing
	@rm -rf ./build/transcoding

make_hello: clean
	gcc -Wall -L/opt/ffmpeg/lib -I/opt/ffmpeg/include hello_world.c \
	  -lavcodec -lavformat -lavfilter -lavdevice -lswresample -lswscale -lavutil \
	  -o ./build/hello

run_hello: make_hello
	./build/hello small_bunny_1080p_60fps.mp4

make_remuxing: clean
	gcc -Wall -L/opt/ffmpeg/lib -I/opt/ffmpeg/include remuxing.c \
	  -lavcodec -lavformat -lavfilter -lavdevice -lswresample -lswscale -lavutil \
	  -o ./build/remuxing

run_remuxing_ts: make_remuxing
	./build/remuxing small_bunny_1080p_60fps.mp4 remuxed_small_bunny_1080p_60fps.ts

run_remuxing_fragmented_mp4: make_remuxing
	./build/remuxing small_bunny_1080p_60fps.mp4 fragmented_small_bunny_1080p_60fps.mp4 fragmented

make_transcoding: clean
	gcc -g -Wall -L/opt/ffmpeg/lib -I/opt/ffmpeg/include transcoding.c video_debugging.c \
	  -lavcodec -lavformat -lavfilter -lavdevice -lswresample -lswscale -lavutil \
	  -o ./build/transcoding

run_transcoding: make_transcoding
	./build/transcoding small_bunny_1080p_60fps.mp4 bunny_1s_gop.mp4

