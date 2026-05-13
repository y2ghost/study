执行脚本
- ansible-playbook -i inventory playbook-vars.yaml

自定义host变量
- mkdir host_vars
- tee host_vars/db.yaml << EOF
ansible_user: test
ansible_ssh_pass: unsafe_for_test
msg_name: test
EOF

外部变量文件
- ansible-playbook -i inventory playbook-vars.yaml --extra-vars="msg_name=test"

交互式执行脚本
- ansible-playbook -i inventory playbook-prompt-vars.yaml

