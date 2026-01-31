import subprocess


subprocess.run(['ls'])
show_output = subprocess.run(['ls', '-l'], capture_output=True)
print(show_output.stdout)
print('popen example below:')

cmd1 = ['ps', '-ef']
ps = subprocess.Popen(cmd1, stdout=subprocess.PIPE)
cmd2 = ['grep', 'python']
grep = subprocess.Popen(cmd2, stdin=ps.stdout,
                        stdout=subprocess.PIPE, encoding="utf-8")
ps.stdout.close()
show_output, _ = grep.communicate()
msg = show_output.split('\n')
print(msg)
