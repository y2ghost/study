import argparse


def file_parser(input_file, output_file=''):
    print(f'处理 {input_file}')
    print('完成处理')
    if output_file:
        print(f'创建 {output_file}')


def main():
    parser = argparse.ArgumentParser('文件分析器')
    parser.add_argument('--infile', help='输入文件')
    parser.add_argument('--out', help='输出文件')
    args = parser.parse_args()
    if args.infile:
        file_parser(args.infile, args.out)


if __name__ == '__main__':
    main()
