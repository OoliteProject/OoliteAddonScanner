{{OXPNote|Do not edit manually. This list will be replaced every now and then with a new authoritative list.}}

{{OXPNote|Latest version of this index including HTML dump available at https://app.box.com/s/e2yqje0xyj2a0brixd4t7a4ncb0zwqun }}


= ${ expansions?size } Expansions =

<#list expansions><#items as expansion>[[${ expansion.title }]] </#items></#list>

= ${ equipment?size } Equipment =

only visible equipment is listed here

<#list equipment><#items as equipment><#if equipment.visible>[[${ equipment.name }]] </#if></#items></#list>

= ${ ships?size } Ships =

<#list ships><#items as ship><#if !ship.template>[[${ ship.name }]] </#if></#items></#list>

= ${ warnings?size } Warnings =
While going through the list of expansions registered for the Expansion Manager,
some discrepancies were noted. These may have an effect on the completeness of 
the above list.

<#if warnings?has_content>

== Global<#list globalWarnings as w> ==
* ${ w }</#list></#if>
<#list expansions><#items as expansion><#if expansion.warnings?has_content>

== ${ expansion.title }<#list expansion.warnings as w> ==
* ${ w }</#list></#if></#items></#list>