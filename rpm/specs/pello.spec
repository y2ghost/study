Name:           pello
Version:        0.1.1
Release:        1%{?dist}
Summary:        hello world example implemented in python

License:        MIT
URL:            https://example.com/%{name}
Source0:        https://example.com/%{name}/releases/%{name}-%{version}.tar.gz

BuildRequires:  python39
Requires:       python39
Requires:       bash
BuildArch:      noarch

%description
the long-tail description for out hello world example implemented in
python.

%prep
%autosetup

%build
python3 -m compileall pello.py

%install
mkdir -p %{buildroot}%{_bindir}
mkdir -p %{buildroot}/usr/lib/%{name}

cat > %{buildroot}%{_bindir}/%{name} <<-EOF
#!/bin/bash
/usr/bin/python3 /usr/lib/%{name}/%{name}.pyc
EOF

chmod 0755 %{buildroot}%{_bindir}/%{name}
install -m 0644 %{name}.py* %{buildroot}/usr/lib/%{name}/

%files
%license LICENSE
%dir /usr/lib/%{name}/
%{_bindir}/%{name}
/usr/lib/%{name}/%{name}.py*

%changelog
* Fri Sep  8 2023 yy
- first pello package
