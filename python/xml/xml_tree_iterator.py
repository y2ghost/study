from xml.etree.cElementTree import ElementTree


def parse_xml(xml_file):
    tree = ElementTree(file=xml_file)
    print("使用树迭代器进行迭代")
    for elem in tree.iter():
        print(f'{elem.tag=}, {elem.text=}')


if __name__ == '__main__':
    parse_xml('note.xml')
