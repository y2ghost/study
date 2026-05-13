# -*- coding:utf-8 -*-

import xml.etree.ElementTree as ET


def create_xml(xml_file):
    root_element = ET.Element('note_taker')
    note_element = ET.Element('note')
    root_element.append(note_element)
    to_element = ET.SubElement(note_element, 'to')
    to_element.text = 'yy'
    from_element = ET.SubElement(note_element, 'from')
    from_element.text = '备忘录'
    heading_element = ET.SubElement(note_element, 'heading')
    heading_element.text = '预约'
    body_element = ET.SubElement(note_element, 'body')
    body_element.text = '遛狗'

    tree = ET.ElementTree(root_element)
    with open(xml_file, "wb") as fh:
        tree.write(fh, encoding="utf-8")


if __name__ == '__main__':
    create_xml('test_create.xml')
