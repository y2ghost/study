import json


def create_json_file(path, obj):
    with open(path, 'w', encoding="utf-8") as fh:
        json.dump(obj, fh, ensure_ascii=False)


if __name__ == '__main__':
    j = {"menu": {
        "id": "file",
        "value": "文件",
        "popup": {
            "menuitem": [
              {"value": "新建", "onclick": "CreateNewDoc()"},
                {"value": "打开", "onclick": "OpenDoc()"},
                {"value": "关闭", "onclick": "CloseDoc()"}
            ]
        }
    }}
    create_json_file('test.json', j)
