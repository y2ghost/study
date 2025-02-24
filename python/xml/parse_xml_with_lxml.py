# 安装模块lxml
# python -m pip install lxml

from lxml import etree, objectify


def parse_xml(xml_file):
    with open(xml_file, "rb") as f:
        xml = f.read()

    root = objectify.fromstring(xml)
    to = root.note.to
    print(f'给谁: {to=}')

    for note in root.getchildren():
        for note_element in note.getchildren():
            print(f'{note_element.tag=}, {note_element.text=}')
        print()

    print(f'原始: {root.note.to=}')
    root.note.to = 'yt'
    print(f'修改: {root.note.to=}')
    root.note.new_element = "我是新来的!"
    objectify.deannotate(root)
    etree.cleanup_namespaces(root)
    obj_xml = etree.tostring(root, pretty_print=True, encoding="utf-8")

    with open("lxml_output.xml", "wb") as f:
        f.write(obj_xml)


if __name__ == '__main__':
    parse_xml('note.xml')
