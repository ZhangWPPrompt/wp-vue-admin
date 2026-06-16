INSERT INTO sys_user (id, username, password, nickname, email, phone, status) VALUES
(1, 'admin', '$2b$12$7qJuj6TzmZ6n4mE8XaW/q.Ze9cLwjMkGE71kDvpBYjoML2zb7ODMS', '超级管理员', 'admin@example.com', '13800000000', 1);

INSERT INTO sys_role (id, role_name, role_code, description, status) VALUES
(1, '超级管理员', 'SUPER_ADMIN', '拥有所有权限', 1),
(2, '普通用户', 'USER', '普通用户权限', 1);

INSERT INTO sys_user_role (user_id, role_id) VALUES (1, 1);

INSERT INTO sys_menu (id, parent_id, menu_name, menu_type, path, component, icon, sort_order, permission, visible, status) VALUES
(1, 0, '系统管理', 'DIR', '/system', '', 'Setting', 1, '', 1, 1),
(2, 1, '用户管理', 'MENU', '/system/user', 'system/user/index', 'User', 1, 'sys:user:list', 1, 1),
(10, 2, '用户新增', 'BUTTON', '', '', '', 1, 'sys:user:add', 0, 1),
(11, 2, '用户编辑', 'BUTTON', '', '', '', 2, 'sys:user:edit', 0, 1),
(12, 2, '用户删除', 'BUTTON', '', '', '', 3, 'sys:user:delete', 0, 1),
(3, 1, '角色管理', 'MENU', '/system/role', 'system/role/index', 'Avatar', 2, 'sys:role:list', 1, 1),
(13, 3, '角色新增', 'BUTTON', '', '', '', 1, 'sys:role:add', 0, 1),
(14, 3, '角色编辑', 'BUTTON', '', '', '', 2, 'sys:role:edit', 0, 1),
(15, 3, '角色删除', 'BUTTON', '', '', '', 3, 'sys:role:delete', 0, 1),
(4, 1, '菜单管理', 'MENU', '/system/menu', 'system/menu/index', 'Menu', 3, 'sys:menu:list', 1, 1),
(16, 4, '菜单新增', 'BUTTON', '', '', '', 1, 'sys:menu:add', 0, 1),
(17, 4, '菜单编辑', 'BUTTON', '', '', '', 2, 'sys:menu:edit', 0, 1),
(18, 4, '菜单删除', 'BUTTON', '', '', '', 3, 'sys:menu:delete', 0, 1),
(5, 0, '系统工具', 'DIR', '/tool', '', 'Tools', 2, '', 1, 1),
(6, 5, '字典管理', 'MENU', '/tool/dict', 'tool/dict/index', 'Collection', 1, 'sys:dict:list', 1, 1),
(19, 6, '字典新增', 'BUTTON', '', '', '', 1, 'sys:dict:add', 0, 1),
(20, 6, '字典编辑', 'BUTTON', '', '', '', 2, 'sys:dict:edit', 0, 1),
(21, 6, '字典删除', 'BUTTON', '', '', '', 3, 'sys:dict:delete', 0, 1),
(7, 0, '日志管理', 'DIR', '/log', '', 'Document', 3, '', 1, 1),
(8, 7, '操作日志', 'MENU', '/log/operation', 'log/operation/index', 'Notebook', 1, 'sys:log:list', 1, 1),
(9, 0, '首页', 'MENU', '/dashboard', 'dashboard/index', 'HomeFilled', 0, '', 1, 1);

INSERT INTO sys_role_menu (role_id, menu_id) VALUES
(1, 1), (1, 2), (1, 10), (1, 11), (1, 12),
(1, 3), (1, 13), (1, 14), (1, 15),
(1, 4), (1, 16), (1, 17), (1, 18),
(1, 5), (1, 6), (1, 19), (1, 20), (1, 21),
(1, 7), (1, 8),
(1, 9);

INSERT INTO sys_dict (id, dict_name, dict_code, description, status) VALUES
(1, '用户状态', 'user_status', '用户状态：启用/禁用', 1),
(2, '菜单类型', 'menu_type', '菜单类型：目录/菜单/按钮', 1);

INSERT INTO sys_dict_item (id, dict_id, item_label, item_value, sort_order, status) VALUES
(1, 1, '启用', '1', 1, 1),
(2, 1, '禁用', '0', 2, 1),
(3, 2, '目录', 'DIR', 1, 1),
(4, 2, '菜单', 'MENU', 2, 1),
(5, 2, '按钮', 'BUTTON', 3, 1);
