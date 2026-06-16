INSERT INTO sys_user (id, username, password, nickname, email, phone, status) VALUES
(1, 'admin', '$2b$12$7qJuj6TzmZ6n4mE8XaW/q.Ze9cLwjMkGE71kDvpBYjoML2zb7ODMS', '超级管理员', 'admin@example.com', '13800000000', 1);

INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限', 1),
(2, '普通用户', 'USER', '普通用户权限', 1);

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible, status) VALUES
(1, 0, '系统管理', 'DIR', '/system', '', 'Setting', 1, '', 1, 1),
(2, 1, '用户管理', 'MENU', '/system/user', 'system/user/index', 'User', 1, 'sys:user:list', 1, 1),
(3, 1, '角色管理', 'MENU', '/system/role', 'system/role/index', 'Avatar', 2, 'sys:role:list', 1, 1),
(4, 1, '菜单管理', 'MENU', '/system/menu', 'system/menu/index', 'Menu', 3, 'sys:menu:list', 1, 1),
(5, 0, '系统工具', 'DIR', '/tool', '', 'Tools', 2, '', 1, 1),
(6, 5, '字典管理', 'MENU', '/tool/dict', 'tool/dict/index', 'Collection', 1, 'sys:dict:list', 1, 1),
(7, 0, '日志管理', 'DIR', '/log', '', 'Document', 3, '', 1, 1),
(8, 7, '操作日志', 'MENU', '/log/operation', 'log/operation/index', 'Notebook', 1, 'sys:log:list', 1, 1),
(9, 0, '首页', 'MENU', '/dashboard', 'dashboard/index', 'HomeFilled', 0, '', 1, 1);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9);

INSERT INTO sys_dict (id, dict_name, dict_code, description, status) VALUES
(1, '用户状态', 'user_status', '用户状态：启用/禁用', 1),
(2, '菜单类型', 'menu_type', '菜单类型：目录/菜单/按钮', 1);

INSERT INTO sys_dict_item (id, dict_id, item_label, item_value, sort_order, status) VALUES
(1, 1, '启用', '1', 1, 1),
(2, 1, '禁用', '0', 2, 1),
(3, 2, '目录', 'DIR', 1, 1),
(4, 2, '菜单', 'MENU', 2, 1),
(5, 2, '按钮', 'BUTTON', 3, 1);
