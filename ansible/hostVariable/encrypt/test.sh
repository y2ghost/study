ansible-playbook -i inventory \
    --extra-vars "@prod.yaml" \
    --ask-vault-pass playbook-enc.yaml

