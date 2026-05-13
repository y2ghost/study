下载角色
- ansible-galaxy install geerlingguy.java

自定义角色
- ansible-galaxy role init printfile
- tee printfile/tasks/main.yml << EOF
- name: Display file contents
  command: "cat {{show_file}}"
  register: command_output
- name: Print to console
  debug:
    msg: "{{command_output.stdout}}"
EOF
- tee printfile/vars/main.yml << EOF
show_file: /etc/hosts
EOF
- 打包文件: tar -czvf printfile.tar.gz printfile
- 拷贝文件: cp printfile.tar.gz /tmp/
- 安装role包: ansible-galaxy role install -r roles/requirements.yml
- 创建目录结构
├── inventory
├── roles
│   └── requirements.yml
└── role_test.yaml
- 测试role: ansible-playbook -i inventory role_test.yaml

collection定义
- ansible-galaxy collection init dev.mycollection
- cd dev/mycollection
- ansible-galaxy collection build
- ansible-galaxy collection install dev-mycollection-1.0.0.tar.gz

