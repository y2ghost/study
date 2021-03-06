import argparse


def file_parser(input_file, output_file=''):
    print(f'处理 {input_file}')
    print('完成处理')
    if output_file:
        print(f'创建 {output_file}')


def main():
    parser = argparse.ArgumentParser('文件分析器',
                                     description='PyParse—文件处理器',
                                     epilog='感谢使用本软件!',
                                     add_help=False)
    group = parser.add_mutually_exclusive_group()
    group.add_argument('-i', '--infile', help='输入文件')
    group.add_argument('-o', '--out', help='输出文件')
    args = parser.parse_args()
    if args.infile:
        file_parser(args.infile, args.out)


if __name__ == '__main__':
    main()
