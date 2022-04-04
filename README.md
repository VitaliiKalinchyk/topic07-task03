В качестве базы данных использовать реляционную БД.
БД содержит три таблицы:
```
users (id, login)
teams (id, name)
users_teams (user_id, team_id)
```
Изначально таблицы БД должны иметь некоторое наполнение (см. код класса Demo)
В корне создать каталог sql и сохранить в нем скрипт создания базы данных `db-create.sql`

Создать и реализовать типы таким образом, чтобы при запуске класса Demo 
отрабатывала соответствующая функциональность.
--------------------------------------------------
Пакет: `com.epam.rd.java.basic.topic07.task03`
Классы: 
Demo - содержит демонстрацию функционала, оставить без изменений.
Остальные классы см. репозиторий.
--------------------------------------------------
Содержимое класса Demo:
```
public class Demo {

	public static void main(String[] args) throws DBException {
		// users  ==> [ivanov, petrov]
		// teams  ==> [teamA teamB teamC]
		// teamA contains the following users: ivanov, petrov 
		
		DBManager dbManager = DBManager.getInstance();
		User userPetrov = dbManager.getUser("petrov");
		System.out.println((dbManager.getUserTeams(userPetrov)));
		// [teamA]

		Team teamA = dbManager.getTeam("teamA");
		Team teamC = dbManager.getTeam("teamC");
		System.out.println((dbManager.getTeamUsers(teamA)));
		// [ivanov, petrov]
		
		// on delete cascade!
		dbManager.deleteTeam(teamA);
		teamC.setName("teamX");
		dbManager.updateTeam(teamC);
		System.out.println((dbManager.findAllTeams()));
		// teams ==> [teamB, teamX]
		
		for (Team team : dbManager.findAllTeams()) {
			dbManager.deleteTeam(team);
		}

		dbManager.insertTeam(Team.createTeam("teamB"));
		System.out.println((dbManager.findAllTeams()));
		// teams ==> [teamB]        
		
		User userIvanov = dbManager.getUser("ivanov");
		System.out.println((dbManager.getUserTeams(userIvanov)));
		// teams ==> []
	}
}

```
(1) Метод DBManager#`setTeamsForUser(User, Team...)` реализовать с помощью 
транзакции: вследствие вызова данного метода пользователю будут назначены либо 
все группы, указаннные в списке аргументов, либо ни одна из них.

Если метод будет вызван так: `setTeamsForUser(user, teamA, teamB, teamC)`,
то в таблицу связей `users_teams` записи должны быть вставлены последовательно 
в порядке появления групп в списке аргументов слева направа:
```
user_id, teamA_id
user_id, teamB_id 
user_id, teamC_id
```
Если последняя запись не может быть добавлена, то первые две также не должны попасть
в базу данных.

(2) Метод DBManager#`getUserTeams` должен возвращать объект `List<Team>`.

(3) Метод DBManager#`deleteTeam` удаляет группу по имени.
Все дочерние записи из таблицы users_teams также должны быть удалены.
Последнее реализовать с помощью каскадного ограничения ссылочной целостности:
`ON DELETE CASCADE`.

(4) Метод DBManager#`updateTeam(Team team)` обновляет группу в базе данных.

(5) Метод DBManager#`insertUser` должен модифицировать поле id объекта User.

(6) Метод DBManager#`findAllUsers` возвращает объект `java.util.List<User>`.

(7) Метод DBManager#`insertTeam` должен модифицировать поле id объекта Team.

(8) Метод DBManager#`findAllTeams` возвращает объект `java.util.List<Team>`.

##### Замечание.
 Класс User должен содержать:
- метод `getLogin()`, который возвращает логин пользователя;
- метод `toString()`, который возвращает логин пользователя;
- реализацию метода `equals(Object obj)`, согласно которой два объекта User 
равны тогда и только тогда, когда они имеют один логин;
- статический метод `createUser(String login)`, который создает объект User по 
логину (идентификатор равен 0).

Класс Team должен содержать:
- метод `getName()`, который возвращает название группы;
- метод `toString()`, который возвращает название группы;
- реализацию метода `equals(Object obj)`, согласно которой два объекта Team 
равны тогда и только тогда, когда они имеют одно название.
- статический метод `createTeam(String name)`, который создает объект Team по 
имени (идентификатор равен 0).
