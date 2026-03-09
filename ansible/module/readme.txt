使用模块
- 配置环境变量ANSIBLE_LIBRARY 
- 拷贝模块文件至~/.ansible/plugins/modules/
- 拷贝模块文件至/usr/share/ansible/plugins/modules/
- 示例: ANSIBLE_LIBRARY=./library ansible-playbook -i inventory playbook.yaml

