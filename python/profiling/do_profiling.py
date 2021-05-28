import profile_test
import cProfile

# 可以命令行执行: python -m cProfile -o profile_output.txt profile_test.py
cProfile.run("profile_test.main()", "profile_output.txt")
