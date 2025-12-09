create database java5
drop table Loai
drop table TaiKhoan
drop table ChiTietDonHang
drop table DonHang
drop table logers
drop table YeuThich
drop table SanPham
CREATE TABLE taikhoan (
	id INT PRIMARY KEY IDENTITY,
	email NVARCHAR(50),
	matkhau NVARCHAR(255),
	hoten NVARCHAR(60),
	hinh NVARCHAR(255),
	kichhoat BIT,
	quantri BIT
);

CREATE TABLE donhang (
	id INT PRIMARY KEY IDENTITY,
	taikhoan_id INT FOREIGN KEY REFERENCES taikhoan(id),
	ten_khach_hang nvarchar(250),
	ngaytao DATE,
	diachi NVARCHAR(250),
	trangthai INT
);

CREATE TABLE danhmuc (
	id INT PRIMARY KEY IDENTITY,
	ten NVARCHAR(250)
);

CREATE TABLE sanpham (
	id INT PRIMARY KEY IDENTITY,
	ten NVARCHAR(250),
	hinh NVARCHAR(250),
	gia INT,
	kichthuoc VARCHAR,
	ngaytao DATE,
	conhang BIT,
	soluong INT,
	danhmuc_id INT FOREIGN KEY REFERENCES danhmuc(id)
);

CREATE TABLE chitietdonhang (
	id INT PRIMARY KEY IDENTITY,
	donhang_id INT FOREIGN KEY REFERENCES donhang(id),
	sanpham_id INT FOREIGN KEY REFERENCES sanpham(id),
	gia INT,
	soluong INT
);

CREATE TABLE yeuthich (
	id INT PRIMARY KEY IDENTITY,
	taikhoan_id INT FOREIGN KEY REFERENCES taikhoan(id),
	sanpham_id INT FOREIGN KEY REFERENCES sanpham(id),
	ngaythich DATE
);

--BỔ SUNG
ALTER TABLE hoadon ADD  donhang_id INT;
ALTER TABLE hoadon ADD  taikhoan_id INT;

ALTER TABLE hoadon ADD CONSTRAINT fk_hoadon_donhang
FOREIGN KEY (donhang_id) REFERENCES donhang(id);

ALTER TABLE hoadon ADD CONSTRAINT fk_hoadon_taikhoan
FOREIGN KEY (taikhoan_id) REFERENCES taikhoan(id);
